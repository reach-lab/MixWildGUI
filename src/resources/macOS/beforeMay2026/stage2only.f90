program stage2only
    use lsboth
    implicit none
    call readdef2()
    call writedef2()
    call readat2()
    call readebfile()
    call adjustdata2()
    if(nreps > 0) then
        call make_random()
    else
        allocate(simvals(1,nc2,numre))
        simvals(1,:,:) = thetas(:,:)
        nreps = 1
    end if
    call dostage2()
end program stage2only

subroutine readdef2()
    use lsboth
    use procedures
    implicit none
    INTEGER :: I,j,k
    
    OPEN(1, FILE='stage2only.def')
    READ(1,'(18A4)') HEAD
    READ(1,*)FILEDAT2
    READ(1,*)EBFILE
    READ(1,*)FILEprefix
    filedef = trim(fileprefix)//".def"
    fileout = trim(fileprefix)//".out"
    filedat2 = adjustl(filedat2)
    ebfile = adjustl(ebfile)

    READ(1,*) NVARsep,nc2,numloc,nreps, nors, myseed, stage2, multi2nd,Ymiss
    miss = 1
    if(fp_equal(ymiss, 0.0d0)) miss = 0
    nors0 = nors
    if(nors .eq. 2) then
        numrs = 2
        nors = 0
    else if(nors .eq. 1) then
        numrs = 0
    else
        nors = 0
        numrs = 1
    end if
    numre = numloc+numrs
    if(multi2nd .ne. 1) multi2nd = 0
    sepfile = 1
    select case(stage2)
        case(1,3)     
            readcats = 0
        case(2,4) 
            readcats = 1
        case default
            stage2 = 0
            nvar2 = 0
            nvar3 = 0
    end select
    if(stage2 .ne. 0) then
        if(numrs .eq. 0) then
            pomega = -1
            pto = -1
            read(1,*) pfixed, ptheta
        else
            read(1,*) pfixed,ptheta,pomega,pto
        end if
        nvar2 = 1+max(pfixed,0)+max(pomega,0)+max(ptheta,0)+max(pto,0)
        nvar3 = 2+pfixed+(pomega+1)*numrs+numloc*(ptheta+1)+(pto+1)*numrs*numloc

        allocate(var2ind(nvar2))
        allocate(var2label(nvar2))
        if(readcats .eq. 1) then
            read(1,*) maxj
            allocate(icode(maxj))
            READ(1,*)(ICODE(J), J = 1,MAXJ)
        end if
        read(1,*) id2indsep,var2ind(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            READ(1,*) (var2ind(k+I), I=1,Pfixed)
            k = k + pfixed
         END IF
        IF (Ptheta .GE. 1) THEN
            READ(1,*) (var2IND(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            READ(1,*) (var2IND(k+I), I=1,Pomega)
            k = k + pomega
         END IF
        IF (Pto .GE. 1) THEN
            READ(1,*) (var2IND(k+I), I=1,Pto)
         END IF

    ! read in the labels
         READ(1,*) var2label(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            READ(1,*) (var2label(k+I), I=1,Pfixed)
            k = k + pfixed
         END IF
        IF (Ptheta .GE. 1) THEN
            READ(1,*) (var2label(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            READ(1,*) (var2label(k+I), I=1,Pomega)
            k = k + pomega
         END IF
        IF (Pto .GE. 1) THEN
            READ(1,*) (var2label(k+I), I=1,Pto)
        END IF
    end if
    CLOSE(1)
end subroutine readdef2

subroutine writedef2
    use lsboth
    implicit none
    INTEGER :: I,j,k
    OPEN(1,FILE=FILEDEF)
    WRITE(1,'(18A4)') HEAD
    write(1,'(A80)')FILEDAT2
    write(1,'(A80)')EBFILE
    write(1,'(A80)')FILEprefix

    write(1,*) NVAR2,numloc,nreps, nors0, myseed, stage2, multi2nd,miss

    select case(stage2)
        case(1,3)     
            readcats = 0
        case(2,4) 
            readcats = 1
        case default
            stage2 = 0
            nvar2 = 0
            nvar3 = 0
    end select
    if(stage2 .ne. 0) then
        if(numrs .eq. 0) then
            pomega = -1
            pto = -1
            write(1,*) pfixed, ptheta
        else
            write(1,*) pfixed,ptheta,pomega,pto
        end if

        if(readcats .eq. 1) then
            write(1,*) maxj
            write(1,*)(ICODE(J), J = 1,MAXJ)
        end if
        write(1,*) id2indsep,var2ind(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            write(1,'(20I3)') (var2ind(k+I), I=1,Pfixed)
            k = k + pfixed
         END IF
        IF (Ptheta .GE. 1) THEN
            write(1,'(20I3)') (var2IND(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            write(1,'(20I3)') (var2IND(k+I), I=1,Pomega)
            k = k + pomega
         END IF
        IF (Pto .GE. 1) THEN
            write(1,'(20I3)') (var2IND(k+I), I=1,Pto)
         END IF

         write(1,*) var2label(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pfixed)
            k = k + pfixed
         END IF
        IF (Ptheta .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pomega)
            k = k + pomega
         END IF
        IF (Pto .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pto)
        END IF
    end if
    CLOSE(1)
end subroutine writedef2

subroutine adjustdata2()
    use lsboth
    use procedures
    implicit none
    integer:: ko,i
    logical :: yislevel2
    yislevel2 = .FALSE.

    ko = 0
    do i=1,nc2sep
        ko = ko + idnisep(i,2)
        if(.not. fp_equal(data2(ko,1), tempsums(i,1))) yislevel2 = .TRUE.
    end do
    if(.not. yislevel2) multi2nd = 0
    
end subroutine adjustData2

subroutine readebfile()
    use lsboth
    use procedures
    implicit none
    integer:: k,i,numre2
    

    numre = numloc + numrs
    numre2 = numre*(numre+1)/2

    allocate(thetas(nc2,numre))
    allocate(thetavs(nc2,numre2))
    allocate(idni(nc2,2))
    allocate(allzeros(nc2))
    allzeros = 0
    
    open(1, file=ebfile)

    do k=1,nc2
        read(1, *) idni(k,1),(thetas(k,i),i=1,numre),(thetavs(k,i),i=1,numre2)
        if(sum(thetas(k,:))+sum(thetavs(k,:)) .eq. 0) allzeros(k) = 1
    end do
    close(1)
!    write(*,*) nc2
!    write(*,*) idni(1:nc2,1)
end subroutine readebfile

subroutine dostage2
    use lsboth
    use procedures
    implicit none
    CHARACTER(LEN=80) :: filetempdat,temp80
    integer :: i,n,j,k,diffk,totalvar,catcount(maxj),ni(nc2),myobs,nvalid,&
                jlabel,jval,nvarpercat,nobs2,ncolinvar,nc2a,&
                myorder(nobssep),mymatch,thisnum,nobsbefore,checknum
    REAL(KIND=8),ALLOCATABLE:: temp3(:),liks(:),tempdata(:),&
                                betas(:,:),meanbetas(:),totalvarhats(:),varbetas(:)
    real(kind=8) :: logl,loglsd,zval,pval,meany,miny,maxy,temp,stdy,seval
    character(len=24) :: mystr,progname
    REAL(KIND=8),ALLOCATABLE:: tempVector(:)

    ni = 0
    nvalid = 0
    nc2a = 0
    myorder = 0
    write(*,*) "There are ",sum(allzeros),"subjects with unestimable random effects"
    write(*,*)
    if(multi2nd .eq. 1) write(*,*) "Multilevel second stage"
        j=0
        do i=1,nc2sep
            mymatch = -1
            if(idni(i+j,1) .eq. idnisep(i,1)) then
                ni(i+j) = idnisep(i,2)
                mymatch = i+j
                j = j - 1
            else
                do k=1,nc2
                    if(idnisep(i,1) .eq. idni(k,1)) then
                        mymatch = k
                        exit
                    end if
                end do
            end if
            if(mymatch .ne. -1) then
                thisnum = 1
                nobsbefore = i-1
                if(multi2nd .eq. 1) then
                    thisnum = idnisep(i,2)
                    nobsbefore = sum(idnisep(1:(i-1),2))
                end if
                ni(mymatch) = thisnum
                myorder((nobsbefore+1):(nobsbefore+thisnum)) = mymatch
                nc2a = nc2a + 1
                j = j+1
            else
                j = j-1
            end if
        end do
        nobs2 = sum(ni)
        if(multi2nd .ne. 1) data2(1:nc2sep,1:nvar2) = tempsums(1:nc2sep,1:nvar2)
!        write(*,*) myorder
!        write(*,*) ni
    nvarpercat = nvar3-1+multi2nd
!    write(*,*) nobs,nc2,nobs2,nc2a
        allocate(tempdata(nvar3))
        allocate(intLabel(nvar3+2+max(maxj-1,0)))
        intlabel(1) = var2label(1)
        intlabel(2) = "Intercept             "
        intlabel(3:pfixed+2) = var2label(2:1+pfixed)
        do j=1,numloc
            write(mystr, '(A9, I1, A14)') "Location_",j,"                 "
            intLabel(1+Pfixed+1+(Ptheta+1)*(j-1)+1) = mystr
            do i=1,pTheta
                write(mystr, '(A6, I1, A17)') "Locat_",j,"*"//var2label(1+pfixed+i)
                intlabel(1+pfixed+1+(Ptheta+1)*(j-1)+1+i) = mystr
            end do
        end do
        do j=1,numrs
            write(mystr, '(A6, I1, A17)') "Scale_",j,"                 "
            intLabel(1+Pfixed+1+(Ptheta+1)*numloc+(1+pomega)*(j-1)+1) = mystr
            do i=1,pOmega
                write(mystr, '(A6, I1, A17)') "Scale_",j,"*"//var2label(1+pfixed+ptheta+i)
                intlabel(1+pfixed+1+(Ptheta+1)*numloc+(1+pomega)*(j-1)+1+i) = mystr
            end do
        end do
        if(pTO >= 0) then
            do j=1,numloc
                do i=1,numrs
                    write(mystr, '(A9, I1, A7, i1, A6)') "Location_",j,"*Scale_",i,"       "
                    intlabel(1+pfixed+1+(1+Ptheta)*numloc+(1+pomega)*numrs+((j-1)*numrs+i-1)*(pto+1)+1) = mystr
                    do k=1, pto
                        write(mystr, '(A2, I1, A3, I1, A17)') "L_",j,"*S_",i,"*"//var2label(1+pfixed+ptheta+pomega+k)
                        intlabel(1+pfixed+1+(1+Ptheta)*numloc+(1+pomega)*numrs+((j-1)*numrs+i-1)*(pto+1)+1+k) = mystr
                    end do
                end do
            end do

        end if
!write(*,*) numloc,numrs,numre
        intLabel(nvar3+1) = "Random.Int.Var"
        intLabel(nvar3+1+multi2nd) = "Residual.Variance"            
        if(stage2 .eq. 2 .and. maxj > 2) then
            intLabel(nvar3+maxj) = "Random.Int.Var"
            do j=1,maxj-1
                write(mystr,'(a10,f8.3,a6)') "Threshold ",icode(j+1),"      "
                intLabel(nvar3+j) = mystr
            end do
        end if

        open(3, file=trim(fileprefix)//'_desc2.out')
         ALLOCATE(tempVector(nobs2))
         

    200  FORMAT(1x,A24,4F12.4)
!    write(3,9) head
!    write(3,*)
    write(3,*) "Level 2 units =",nc2a
    if(multi2nd .eq. 1) write(3,*) "Level 1 observations =",nobs2
        WRITE(3,'("------------")')
         WRITE(3,'("Descriptives")')
         WRITE(3,'("------------")')
         WRITE(3,*)

        if(stage2 .eq. 1 .or. stage2 .eq. 3) then
            meany=SUM(data2(1:nobs2,1))/DBLE(nobs2)
             miny=minval(data2(1:nobs2,1))
             maxy=maxval(data2(1:nobs2,1))
             tempVector(:)=(data2(1:nobs2,1)-meany)**2
             temp=SUM(tempVector)/DBLE(nobs2-1)
             stdy=DSQRT(TEMP)
             WRITE(3,'(" Dependent variable")')
         WRITE(3,'("                                 mean         min         max     std dev")') 
         WRITE(3,'(" ------------------------------------------------------------------------")')
             WRITE(3,200) var2Label(1)//"        ",meany,miny,maxy,stdy
        else
            catcount = 0
			do myobs=1,nobssep
				if(myorder(myobs) .ne. 0) then
					tempdata(1) = data2(myobs,1)
					do j=1,maxj
						if(fp_equal(data2(myobs,1),icode(j))) then
							catcount(j) = catcount(j) + 1
							cycle
						end if
					end do
                end if
            end do
             WRITE(3,'(" Categories of the Dependent Variable")')
             WRITE(3,'(" ----------------------------------------------------------------")')
             WRITE(3,'(" Category             Frequency         Proportion")') 
            DO J = 1,MAXJ
                WRITE(3,"(1X,F8.2,8X,F12.2,8x,f12.5)") iCODE(J),real(catcount(J)),real(catcount(J))/nobs2
            end do
        end if
        
        if(pfixed > 0) then
            write(3,*)
            write(3,*)
             WRITE(3,'(" Independent variables")')
         WRITE(3,'("                                 mean         min         max     std dev")') 
         WRITE(3,'(" ------------------------------------------------------------------------")')

            do i=1,pfixed
                meany=sum(data2(1:nobs2,1+i))/dble(nobs2)
                miny=minval(data2(1:nobs2,1+i))
                maxy=maxval(data2(1:nobs2,1+i))
                tempVector(:)=(data2(1:nobs2,1+i)-meany)**2
                TEMP=SUM(tempVector)/DBLE(nobs2-1)
                stdy=DSQRT(TEMP)
                WRITE(3,200) var2label(i+1)//"        ",meany,miny,maxy,stdy
            end do
        end if

         WRITE(3,*)
         WRITE(3,*)
         WRITE(3,'(" Random Location and Scale EB mean estimates")')
         WRITE(3,'("                                 mean         min         max     std dev")') 
         WRITE(3,'(" ------------------------------------------------------------------------")')
        do j=1,numloc+numrs
            meany=sum(thetas(:,j),ni>0)/dble(nc2a)
            miny=minval(thetas(:,j),ni>0)
            maxy=maxval(thetas(:,j),ni>0)
            tempVector(1:nc2a)=(thetas(:,j)-meany)**2
            TEMP=SUM(tempVector,ni>0)/DBLE(nc2a-1)
            stdy=DSQRT(TEMP)
!write(*,*) temp,stdy,nc2a,tempVector
            write(mystr, '(A9, I1, A14)') "Location_",j,"                 "
            if(j > numloc) write(mystr, '(A6, I1, A17)') "Scale_",j-numloc,"                 "
            if(j .le. numloc .or. (j > numloc .and. nors .ne. 1)) WRITE(3,200) mystr,meany,miny,maxy,stdy
        end do
        if(pto >= 0) then
            do j=1,numloc
                do i=1,numrs
                    meany=sum(thetas(:,j)*thetas(:,numloc+i),ni>0)/dble(nc2a)
                    miny=minval(thetas(:,j)*thetas(:,numloc+i),ni>0)
                    maxy=maxval(thetas(:,j)*thetas(:,numloc+i),ni>0)
                    tempVector(1:nc2a)=(thetas(:,j)*thetas(:,numloc+i)-meany)**2
                    TEMP=SUM(tempVector,ni>0)/DBLE(nc2a-1)
                    stdy=DSQRT(TEMP)
                    write(mystr, '(A6, I1, A7, I1, A9)') "Locat_",j,"*Scale_",i,"                 "
                    WRITE(3,200) mystr,meany,miny,maxy,stdy
                end do
            end do
        end if
         WRITE(3,*)
         write(3,*) "There are",sum(allzeros),"subjects with unestimable random effect values"
         WRITE(3,*)
         WRITE(3,*)
         

         close(3)
            write(mystr, '(I5)') nreps

    
    totalvar = nvar3-1+multi2nd
    select case(stage2)
        case(1)
            progname = "mixreg"
            totalvar = nvar3+multi2nd
        case(2)
            !progname = "mixor"
            !if(multi2nd .eq. 1) 
            progname = "mixors"
            totalvar = nvar3-1+multi2nd+maxj-1
        case(3)
            progname = "mixpreg"
        case(4)
            progname = "mixno"
            totalvar = (maxj-1)*(nvar3-1+multi2nd)
    end select

        filetempdat = trim(fileprefix) // "_.dat"
        OPEN(1,FILE=trim(progname)//".def")
        WRITE(1,*) " x                                                                             "
        WRITE(1,*) " x                                                                              "
        WRITE(1,5) adjustl(filetempdat)
        if(len(trim(fileprefix)) > 34) then
            temp80 = fileprefix(1:34) // "_x.out"
        else
            temp80 = trim(fileprefix) // "_x.out"
        end if
        WRITE(1,5) adjustl(temp80)
    5 FORMAT(A80)

        if(stage2 .ne. 2) WRITE(1,'("temp_.def")')

    select case(stage2)
        case(1)
            write(1,'("temp_.res")')
            WRITE(1,'(5I3,E10.1E3, 9I3)') 1, 5, nvar3+2, multi2nd, nvar3-1, .0005, 0, 0, 0, 0, 0, 0, 1, 0, 0
        case(2)
        !if(multi2nd .ne. 1) then
        !    WRITE(1,'(4I3,E10.1E3,17I3,i4)') 0, nvar3+2, multi2nd, nvar3-1, .0005, maxj, 0, 0,0,0,11,1,1,0,0,0,0,0,1,0,0,0,100
        !else
            WRITE(1,'(9I3,E10.1E3,4I4,E10.1E3,2i2)') nvar3+2, maxj, nvar3-2, 0, 0, 0, 0, 0, 1-multi2nd, .0005, 11,1,100,0,.1,1,1
            write(1,*) 0, -1
        !end if
        case(3)
            WRITE(1,'(4I3,E10.1E3, 12I3)') 1, nvar3+2, multi2nd, nvar3-1, .0005, 0, 0, 0, 0, 0, 0, 11, 0, 0, 1, 0, 0
        case(4)
            WRITE(1,'(4I3,E10.1E3,17I3,i4)') 0, nvar3+2, multi2nd, nvar3-1, .0005, maxj, 0, 0,0,0,0,0,11,0,0,1,0,1,0

    end select
        WRITE(1,*) 1, 2
        if(stage2 .ne. 2) then! .or. multi2nd .ne. 1) then
            if(multi2nd .eq. 1) write(1,*) nvar3+2
            write(1,*) (i+2, i=1, nvar3-1)
        else
            write(1,*) (i+2, i=2, nvar3-1)
        end if
        if(stage2 .eq. 2 .or. stage2 .eq. 4)     write(1,'(20f8.3)')(iCODE(J), J = 1,MAXJ)
        WRITE(1,*) intlabel(1)
        if(stage2 .ne. 2) then! .or. multi2nd .ne. 1) then
            if(multi2nd .eq. 1) write(1,*) "Intercept"
            if(stage2 .eq. 4) then
                write(1,'(12a8)') (intlabel(i), i=2, nvar3)
            else
                write(1,*) (intlabel(i), i=2, nvar3)
            end if
        else
            write(1,*) (intlabel(i), i=3, nvar3)
        end if
        CLOSE(1)

        allocate(betas(nreps,totalvar))
        allocate(totalvarhats(totalvar))
        totalvarhats = 0
        allocate(liks(nreps))
        allocate(temp3(totalvar))
!        write(*,*) trim(progname),nvar3,multi2nd,totalvar
        betas = 0
        liks = 0
        ncolinvar = totalvar
        if(stage2 .eq. 1) ncolinvar = nvar3-1
    do n=1, nreps
        myobs = 0
        if(mod(n,10) .eq. 0 .or. multi2nd .eq. 1) write(*,*) n
        open(n*5,file=filetempdat)
        do myobs=1,nobssep
            if(myorder(myobs) .ne. 0) then
                tempdata(1) = data2(myobs,1)
                k=1
                diffk = 0
                if(pfixed .ge. 0) then
                    tempdata(2) = 1d0
                    diffk = diffk + 1
                    if(pfixed .ge. 1) then
                        tempdata(k+diffk+1:k+diffk+pfixed) = data2(myobs,k+1:k+pfixed)
                        k = k + pfixed
                    end if
                end if
                if(ptheta .ge. 0) then
                    do j=1,numloc
                        tempdata(k+diffk+1) = simvals(n,myorder(myobs),j)
                        diffk = diffk + 1
                        if(ptheta .ge. 1) then
                            tempdata(k+diffk+1:k+diffk+ptheta) = simvals(n,myorder(myobs),j)*data2(myobs,k+1:k+ptheta)
                            if(j .ne. numloc) diffk = diffk + ptheta
                        end if
                    end do
                    k = k + ptheta
                end if
                if(pomega .ge. 0) then
                    do j=1,numrs
                        tempdata(k+diffk+1) = simvals(n,myorder(myobs),numloc+j)
                        diffk = diffk + 1
                        if(pomega .ge. 1) then
                            tempdata(k+diffk+1:k+diffk+pomega) = simvals(n,myorder(myobs),numloc+j)*data2(myobs,k+1:k+pomega)
                            if(j .ne. numrs) diffk = diffk + pomega
                        end if
                    end do
                    k = k + pomega
                end if
                if(pto .ge. 0) then
                    do j=1,numloc
                        do i=1,numrs
                            tempdata(k+diffk+1) = simvals(n,myorder(myobs),j)*simvals(n,myorder(myobs),numloc+i)
                            diffk = diffk + 1
                            if(pto .ge. 1) then
                                tempdata(k+diffk+1:k+diffk+pto) = simvals(n,myorder(myobs),j)*simvals(n,myorder(myobs),numloc+i)*&
                                    data2(myobs,k+1:k+pto)
                                !if(j .ne. numloc .or. i .ne. numrs) 
                                diffk = diffk + pto
                            end if
                        end do
                    end do
                    k = k + pto
                end if
                write(n*5,'(i16,25e18.6)') myorder(myobs),(tempdata(k),k=1,nvar3),1.0
            end if
        end do
        close(n*5)
        CALL SYSTEM(progname//"> _temp")
            open(n*5+4, file=trim(progname)//".lik")
            read(n*5+4,*) liks(n),checknum
            close(n*5+4)
        if((stage2 .ne. 2 .and. checknum .eq. totalvar) .or. (stage2 .eq. 2 .and. checknum<100)) then
            nvalid = nvalid + 1
            open(n*5+2, file=trim(progname)//".est")
            do j=1,totalvar
                read(n*5+2,*) mystr, betas(nvalid,j)
            end do
            close(n*5+2)
            if(isnan(betas(nvalid,1))) then
                nvalid = nvalid - 1
                write(*,*) "INVALID REPLICATION"
            else
                open(n*5+3, file=trim(progname)//".var")
                    do j=1,ncolinvar
                        read(n*5+3,*) (temp3(i),i=1,ncolinvar)
                        totalvarhats(j) = totalvarhats(j) + temp3(j)
                    end do
                    if(stage2 .eq. 1) then
                        do j=1,1+multi2nd
                            read(n*5+3,*) (temp3(i),i=1,1+multi2nd)
                            totalvarhats(nvar3-1+j) = totalvarhats(nvar3-1+j) + temp3(j)
                        end do
                    end if
                close(n*5+3)
            end if
        else
            write(*,*) checknum
        end if
    end do
            write(mystr, '(I5)') nvalid
        fileout = trim(fileprefix) // "_"//trim(adjustl(mystr)) //"x.out"
    open(2,file=fileout)
    write(2,*)
!write(2,*) "Level 2 obervations =",nc2
    write(2,*) "Number of replications =", nvalid
    write(2,*)
    WRITE(2,'("-------------")')
    WRITE(2,'("Final Results")')
    WRITE(2,'("-------------")')
    logl = sum(liks)/nvalid
    allocate(meanbetas(totalvar+1))
    do j=1,totalvar
        meanbetas(j) = sum(betas(1:nvalid,j))/nvalid
    end do

if(nvalid .eq. 1) nvalid = 2
    loglsd = sqrt(sum((liks(1:min(nvalid,nreps))-logl)**2)/(nvalid-1))
   WRITE(2,562)LOGL,loglsd,LOGL-totalvar+1, &
   LOGL-0.5D0*DBLE(totalvar-1)*DLOG(DBLE(NC2)),0-2*LOGL,0-2*(LOGL-totalvar+1), &
   0-2*(LOGL-0.5D0*DBLE(totalvar-1)*DLOG(DBLE(NC2)))
   562 FORMAT(1X,'Average Log Likelihood         = ',F12.3,' (sd=',F12.3,')'/, &
         1X,"Akaike's Information Criterion = ",F12.3,/, &
         1X,"Schwarz's Bayesian Criterion   = ",F12.3,//,&
         1X,"==> multiplied by -2             ",      /  &
         1X,'Log Likelihood                 = ',F12.3,/, &
         1X,"Akaike's Information Criterion = ",F12.3,/, &
         1X,"Schwarz's Bayesian Criterion   = ",F12.3,/)
            WRITE(2,57)
            57 FORMAT(/,'Variable',20x,'    Estimate',4X,'AsymStdError',4x, &
                      '     z-value',4X,'     p-value',/,'------------------------',4x,  &
                      '------------',4X,'------------',4X,'------------',4X,'------------')
    allocate(varbetas(totalvar+1))
    varbetas = 0
    totalvarhats(:) = totalvarhats(:) / (nvalid-1)

     804 FORMAT(A24,4(4x,F12.5))
        681 FORMAT(1X,'Category',F8.3,' vs Category',F8.3,/)
    do j=1,totalvar !nvar-1 fixed effects plus the residual variance
        if(stage2 .eq. 4 .and. mod(j,nvar3-1+multi2nd) .eq. 1)   write(2,681) icode(j/(nvarpercat)+2),icode(1)

        do i=1,min(nvalid,nreps)
            varbetas(j) = varbetas(j) + (betas(i,j)-meanbetas(j))**2
        end do
        varbetas(j) = varbetas(j) / (nvalid - 1)
        jlabel = j+1
        jval = j
        if(stage2 .eq. 4) then
            jlabel = mod(j,nvarpercat)+1
            jval = j - j/nvarpercat
            if(jlabel .eq. 1) then
                jlabel = nvarpercat+1
                jval = totalvar - maxj+1+j/nvarpercat
            end if
        end if
        if(intLabel(jlabel) .eq. "Random.Int.Var") write(2,*)
        seval = sqrt(totalvarhats(jval)+varbetas(jval)*(1+1/nvalid))
        ZVAL = meanbetas(jval)/seval
        PVAL = 2.0D0 *(1.0D0 - PHIFN(DABS(ZVAL),0))        
        if(stage2 .ne. 2) then
            write(2,804) intlabel(jlabel), meanbetas(jval), seval,zval,pval
        else if(maxj .eq. 2) then
            if(j .ne. nvar3) write(2,804) intlabel(jlabel), meanbetas(jval), seval,zval,pval
        else
            if(j .ne. 1) write(2,804) intlabel(jlabel), meanbetas(jval), seval,zval,pval
        end if
        if(stage2 .eq. 4 .and. mod(j,nvar3-1+multi2nd) .eq. 0) write(2,*)
    end do
    write(2,*)
    write(2,*)
    write(2,*) "L_1 = Locat_1 = Location_1 = 1st location random effect"
    if(numloc>1) write(2,*) "L_2 = Locat_2 = Location_2 = 2nd location random effect"
    if(nors .ne. 1) write(2,*) "S_1 = Scale_1 = 1st scale random effect"
    if(numrs > 1) write(2,*) "Scale_2 = 2nd scale random effect"
close(2)
        CALL SYSTEM("copy "//trim(fileprefix)//"_desc2.out+"//fileout//" "//trim(fileprefix)//".out")
        CALL SYSTEM("mkdir work")
    call system("move "//trim(fileprefix)//"_* work")
    call system("move "//trim(fileprefix)//"*_x.out temp_.* work")
    call system("move "//trim(progname)//".var "//trim(progname)//".est "//trim(progname)//".lik "//trim(progname)//".def work")
end subroutine dostage2

subroutine make_random
    use lsboth
    use procedures
    implicit none
    
    INTEGER :: I,k,j,c
    REAL(KIND=8),ALLOCATABLE:: chols(:,:,:),myrand(:,:)
    integer,allocatable :: tempseed(:)

    allocate(chols(nc2,numre,numre))
    allocate(simvals(nreps,nc2,numre))
    allocate(myrand(nc2,numre))
    
    call random_seed(size=k)
    allocate(tempseed(k))
    tempseed(:) = myseed
    call random_seed(put=tempseed)

    allzeros = 0
    do k=1,nc2
        c=1
        if(allzeros(k) .ne. 1) then
	        do i=1,numre
	            do j=1,i
	                chols(k,i,j) = thetavs(k,c)
	                c = c+1
	                chols(k,j,i) = chols(k,i,j)
	            end do
	        end do
            call cholesky_sub(chols(k,:,:),numre,allzeros(k))
        end if
    end do
!        write(*,'(20f8.3)') ((chols(1,i,j),i=1,numre),j=1,numre)
!        write(*,'(20f8.3)') (thetavs(1,c),c=1,numre*(numre+1)/2)
    do i=1,nreps
        do j=1,nc2
            do k=1,numre
                myrand(j,k) = random_normal()
            end do
        end do
        do k=1,nc2
            simvals(i,k,:) = matmul(myrand(k,:),chols(k,:,:)) + thetas(k,:)
!            write(*,*) k,(simvals(i,j,k),j=1,nc2)
        end do
    end do
end subroutine make_random

SUBROUTINE READAT2()
    use lsboth, only: tempsums,data2,nc2sep,nvar2,idnisep,id2indsep,miss,ymiss,nvarsep,maxksep,nobssep,var2ind,&
                      filedat2
    use procedures
    implicit none

    INTEGER :: myPASS,I,K,ICOUNT,myindex,IDTEMP,IDOLD,hasmiss
    REAL(KIND=8),ALLOCATABLE:: TEMPR(:)
    LOGICAL FIRST

    ALLOCATE (TEMPR(NVARsep))

   ! INITIALIZE
    DO myPASS = 1,2
        IF (myPASS .EQ. 2) THEN
                allocate(tempsums(nc2sep,nvar2))
                allocate(data2(icount,nvar2))

              tempsums = 0
              data2 = 0
        ! IDNI has IDs and Nobs per ID
              ALLOCATE (IDNIsep(NC2sep,2))
              IDNIsep = 0
        ENDIF
   
        I     = 1
        K     = 1
        MAXKsep  = 0
        ICOUNT= 0
        NOBSsep  = 0
        FIRST  = .TRUE.

        ! READ IN DATA UNTIL END
        OPEN(1,ACTION='READ',FILE=FILEDAT2)

        DO   ! loop forever
        
              READ(1,*,END=1999)(TEMPR(myindex),myindex=1,NVARsep)
                hasmiss = 0
                IF (MISS .EQ. 1) THEN
                    do myindex = 1,nvar2
                        IF (FP_EQUAL(tempr(var2ind(myindex)), YMISS)) THEN
                            hasMISS = 1
                            exit
                        END IF
                    end do
                end if
                IF (hasMISS .NE. 0) THEN
                    CYCLE  ! give up on current value and go read next one
                end if

      ! QUERY FOR NEW ID AND SET PARAMETERS ACCORDINGLY

              IDTEMP = INT(TEMPR(ID2INDsep))
              ! QUERY FOR NEW ID AND SET PARAMETERS ACCORDINGLY

              IF (.NOT. FIRST) THEN 
                 IF (IDTEMP .EQ. IDOLD) THEN
                    K     = K+1
                 ELSE
                    if(mypass .eq. 2) then
                        IDNIsep(I,1) = IDOLD
                        IDNIsep(I,2) = K
                    ENDIF
                        NOBSsep = NOBSsep+K
                        IF (K .GT. MAXKsep) MAXKsep = K
                        I     = I+1
                    K     = 1
                 ENDIF
              ENDIF

              ! PUT TEMPORARY VALUES INTO DATA VECTORS AND MATRICES

              IDOLD = IDTEMP
              ICOUNT = ICOUNT+1

              FIRST  = .FALSE.
              IF (myPASS == 2) THEN
                    do myindex=1,nvar2
                        if(var2ind(myindex) .ne. 0) then
                            tempsums(i,myindex) = tempsums(i,myindex) + tempr(var2ind(myindex))
                            data2(icount,myindex) = tempr(var2ind(myindex))
                        end if
                    end do
              END IF

        END DO   ! loop back to read next line
        
    ! cleanup final entry
    1999                IF (myPASS .EQ. 2) THEN
                           IDNIsep(I,1) = IDOLD
                           IDNIsep(I,2) = K
                        ENDIF
                        NOBSsep = NOBSsep+K
                        IF (K .GT. MAXKsep) MAXKsep = K
    NC2sep = I
    CLOSE(1)
   END DO   ! two passes, one to get size, second to read data
        do i=1,nc2sep
            tempsums(i,1:nvar2) = tempsums(i,1:nvar2)/idnisep(i,2)
        end do
   DEALLOCATE(TEMPR)
END SUBROUTINE READAT2
